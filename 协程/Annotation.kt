package com.lonbon.kotlin

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention()
annotation class Annotation(val value: String)