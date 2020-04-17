package resktx.plugin

import com.squareup.kotlinpoet.*
import java.util.*

internal val SUPPORTED_TYPES = setOf("anim", "array", "attr", "bool", "color", "dimen",
    "drawable", "id", "integer", "layout", "menu", "plurals", "string", "style", "styleable")

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
        return FileSpec.get(packageName, result
            .addKdoc("Generated code from Res KTX gradle plugin. Do not modify!")
            .build())
    }

    fun addResourceField(type: String, fieldName: String, fieldInitializer: CodeBlock) {
        if (type !in SUPPORTED_TYPES) {
            return
        }

        val fieldSpecBuilder = getFieldSpecBuilder(type, fieldName, fieldInitializer)

        val resourceType =
            resourceTypes.getOrPut(type) {
                TypeSpec.objectBuilder(type)
            }

        resourceType.addProperty(fieldSpecBuilder.build())
    }

    private fun getFieldSpecBuilder(type: String, fieldName: String, fieldInitializer: CodeBlock): PropertySpec.Builder {
        val inlineType = getInlineTypeNameForType(type)
        return PropertySpec.builder(fieldName, inlineType)
            .mutable(false)
            .addAnnotation(JvmStatic::class.java)
            .addAnnotation(AnnotationSpec.builder(JvmName::class.java)
                .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
                .addMember(CodeBlock.of("name = %S", fieldName))
                .build())
            .initializer(getInlineTypeInitializerForType(inlineType, fieldInitializer))
    }

    private fun getInlineTypeNameForType(type: String): TypeName {
        return ClassName("resktx", type.capitalize(Locale.US) + "Res")
    }

    private fun getInlineTypeInitializerForType(inlineType: TypeName, valueInitializer: CodeBlock): CodeBlock {
        return CodeBlock.of("%T(value = %L)", inlineType, valueInitializer)
    }


    // TODO https://youtrack.jetbrains.com/issue/KT-28933
    private fun String.capitalize(locale: Locale) = substring(0, 1).toUpperCase(locale) + substring(1)

}