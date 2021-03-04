package com.boyzdroizy.library.tooltipopwordtv.annotations

import androidx.annotation.Dimension


@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
@Target(
        AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD,
        AnnotationTarget.LOCAL_VARIABLE
)
@Dimension(unit = Dimension.SP)
internal annotation class Sp
