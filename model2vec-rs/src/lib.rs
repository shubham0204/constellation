mod static_model;

use std::ffi::{c_char, CStr};

use crate::static_model::StaticModel;

#[repr(C)]
pub struct Embedding {
    len: i32,
    data: *mut f32,
}

#[repr(C)]
pub struct EmbeddingsArray {
    len: i32,
    data: *mut Embedding,
}

fn get_str(c_char_buffer: *const c_char) -> String {
    unsafe {
        CStr::from_ptr(c_char_buffer)
            .to_str()
            .expect("Could not convert weights_filepath to &str")
            .to_owned()
    }
}

#[no_mangle]
pub extern "C" fn model2vec_create(
    embeddings_path_cstr: *const c_char,
    tokenizer_path_cstr: *const c_char,
    num_threads: usize,
) -> *mut StaticModel {
    let embeddings_path = get_str(embeddings_path_cstr);
    let tokenizer_path = get_str(tokenizer_path_cstr);
    Box::into_raw(Box::new(
        StaticModel::new(&embeddings_path, &tokenizer_path, num_threads)
            .expect("Could not instantiate StaticModel"),
    ))
}

#[no_mangle]
pub extern "C" fn model2vec_add_seq_buffer(model: *mut StaticModel, sequence_cstr: *const c_char) {
    let model = unsafe { &mut *model };
    let sequence = get_str(sequence_cstr);
    model.add_seq_buffer(sequence);
}

#[no_mangle]
pub extern "C" fn model2vec_clear_seq_buffer(model: *mut StaticModel) {
    let model = unsafe { &mut *model };
    model.clear_seq_buffer();
}

#[no_mangle]
pub extern "C" fn model2vec_encode_seq_buffer(model: *mut StaticModel) -> EmbeddingsArray {
    let model = unsafe { &*model };
    let embeddings = model.encode_seq_buffer().expect("model.encode failed");
    let len = embeddings.len() as i32;
    let mut embeddings_array = Vec::with_capacity(len as usize);
    for embedding in embeddings {
        let embedding_ptr = Box::into_raw(embedding.into_boxed_slice()) as *mut f32;
        embeddings_array.push(Embedding {
            len: model.embedding_dims as i32,
            data: embedding_ptr,
        });
    }
    let embeddings_ptr = Box::into_raw(embeddings_array.into_boxed_slice()) as *mut Embedding;
    EmbeddingsArray {
        len,
        data: embeddings_ptr,
    }
}

#[no_mangle]
pub extern "C" fn model2vec_release(model: *mut StaticModel) {
    unsafe {
        let _ = Box::from_raw(model);
    }
}

#[no_mangle]
pub extern "C" fn model2vec_embedding_dims(model: *const StaticModel) -> usize {
    let model = unsafe { &*model };
    model.embedding_dims
}

#[no_mangle]
pub extern "C" fn model2vec_free_embeddings(embeddings: EmbeddingsArray) {
    unsafe {
        let embeddings_slice =
            std::slice::from_raw_parts_mut(embeddings.data, embeddings.len as usize);
        for embedding in &mut *embeddings_slice {
            if !embedding.data.is_null() {
                let _ = Box::from_raw(embedding.data);
            }
        }
        let _ = Box::from_raw(embeddings_slice as *mut [Embedding]);
        let _ = Box::from_raw(embeddings.data);
    }
}

#[cfg(feature = "android")]
mod jni_bindings {
    extern crate jni;
    use jni::objects::{AsJArrayRaw, JClass, JString};
    use jni::sys::jobjectArray;
    use jni::sys::{jint, jlong};
    use jni::JNIEnv;

    use crate::static_model::StaticModel;

    #[no_mangle]
    pub extern "C" fn Java_io_shubham0204_model2vec_ml_Model2Vec_create(
        mut env: JNIEnv,
        _class: JClass,
        embeddings_path: JString,
        tokenizer_path: JString,
        num_threads: jint,
    ) -> jlong {
        let embeddings_path: String = env
            .get_string(&embeddings_path)
            .expect("Could not get embeddings_path")
            .into();
        let tokenizer_path: String = env
            .get_string(&tokenizer_path)
            .expect("Could not get tokenizer_path")
            .into();
        let static_model =
            StaticModel::new(&embeddings_path, &tokenizer_path, num_threads as usize)
                .expect("Could not instantiate StaticModel");
        Box::into_raw(Box::new(static_model)) as jlong
    }

    #[no_mangle]
    pub extern "C" fn Java_io_shubham0204_model2vec_ml_Model2Vec_addSeqBuffer(
        mut env: JNIEnv,
        _class: JClass,
        model: jlong,
        sequence: JString,
    ) {
        let model = model as *mut StaticModel;
        let sequence: String = env
            .get_string(&sequence)
            .expect("Could not get sequence")
            .into();
        unsafe {
            (*model).add_seq_buffer(sequence);
        }
    }

    #[no_mangle]
    pub extern "C" fn Java_io_shubham0204_model2vec_ml_Model2Vec_clearSeqBuffer(
        _: JNIEnv,
        _class: JClass,
        model: jlong,
    ) {
        let model = model as *mut StaticModel;
        unsafe {
            (*model).clear_seq_buffer();
        }
    }

    #[no_mangle]
    pub extern "C" fn Java_io_shubham0204_model2vec_ml_Model2Vec_encode(
        mut env: JNIEnv,
        _class: JClass,
        model: jlong,
    ) -> jobjectArray {
        let model = model as *mut StaticModel;
        let embeddings = unsafe { (*model).encode_seq_buffer() }.expect("model.encode failed");
        let embedding_dims = unsafe { (*model).embedding_dims };

        let cls = env
            .find_class("[F")
            .expect("Could not find float array class");
        let initial_value = env
            .new_float_array(embedding_dims as i32)
            .expect("Could not create new float array");
        let embeddings_arr = env
            .new_object_array(embeddings.len() as i32, cls, initial_value)
            .expect("Could not create new object array");

        for (i, embedding) in embeddings.iter().enumerate() {
            let embedding_arr = env
                .new_float_array(embedding_dims as i32)
                .expect("Could not create new float array");
            env.set_float_array_region(&embedding_arr, 0, &embedding)
                .expect("Could not set float array region");
            env.set_object_array_element(&embeddings_arr, i as i32, &embedding_arr)
                .expect("Could not set object array element");
            env.delete_local_ref(embedding_arr)
                .expect("Could not delete local reference");
        }

        embeddings_arr.as_jarray_raw()
    }

    #[no_mangle]
    pub extern "C" fn Java_io_shubham0204_model2vec_ml_Model2Vec_release(
        _: JNIEnv,
        _class: JClass,
        model: jlong,
    ) {
        let model = model as *mut StaticModel;
        unsafe {
            let _ = Box::from_raw(model);
        }
    }
}
