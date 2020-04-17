package resktx.plugin

import com.squareup.kotlinpoet.*
import java.util.*

internal val SUPPORTED_TYPES = setOf(
    "anim",
    "animator",
    "array",
    "attr",
    "bool",
    "color",
    "dimen",
    "drawable",
    "font",
    "fraction",
    "id",
    "integer",
    "interpolator",
    "layout",
    "menu",
    "navigation",
    "plurals",
    "raw",
    "string",
    "style",
    "styleable",
    "transition",
    "xml"
)

class KObjectBuilder(
    private val packageName: String,
    private val className: String
) {

    private var resourceTypes = mutableMapOf<String, TypeSpec.Builder>()

    fun build(): FileSpec {
        val result = TypeSpec.objectBuilder(className)

        for (type in SUPPORTED_TYPES) {
            resourceTypes[type]?.let {
                result.addType(it.build())
            }
        }
        return FileSpec.get(
            packageName, result
                .addKdoc("Generated code from Res KTX gradle plugin. Do not modify!")
                .build()
        )
    }

    fun addResourceField(type: String, fieldName: String) {
        if (type !in SUPPORTED_TYPES) {
            return
        }

        val fieldSpecBuilder = getFieldSpecBuilder(type, fieldName)

        val resourceType =
            resourceTypes.getOrPut(type) {
                TypeSpec.objectBuilder(type)
            }

        resourceType.addProperty(fieldSpecBuilder.build())
    }

    private fun getFieldSpecBuilder(
        type: String,
        fieldName: String
    ): PropertySpec.Builder {
        val inlineType = getInlineTypeNameForType(type)
        return PropertySpec.builder(fieldName, inlineType)
            .mutable(false)
            .addAnnotation(JvmStatic::class.java)
            .addAnnotation(
                AnnotationSpec.builder(JvmName::class.java)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
                    .addMember(CodeBlock.of("name = %S", fieldName))
                    .build()
            )
            .initializer(getInlineTypeInitializerForType(inlineType, type, fieldName))
    }

    private fun getInlineTypeNameForType(type: String): TypeName {
        return ClassName("resktx", type.capitalize(Locale.US) + "Res")
    }

    private fun getInlineTypeInitializerForType(
        inlineType: TypeName,
        type: String,
        fieldName: String
    ): CodeBlock {
        return CodeBlock.of("%T(value = R.%L.%L)", inlineType, type, fieldName)
    }

    // TODO https://youtrack.jetbrains.com/issue/KT-28933
    private fun String.capitalize(locale: Locale) =
        substring(0, 1).toUpperCase(locale) + substring(1)

}