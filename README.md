# Res KTX
Res KTX is a library for Android allowing type safe referencing of Resource IDs in Kotlin.

Typically you'd annotate Integer parameters, properties and return types with the AndroidX annotation which best fits it, such as `@ColorRes`. This allows linting to detect when you're using the wrong int, based on annotation in other places, but a major drawback of this is that the lint system isn't able to fail builds immediately if you've opted out of linting, or have incorrectly annotated something. Res KTX generates typed wrappers for IDs, allowing you to be more sure that your code will work at runtime by leaning on the compilers type-checking. You can use Res KTX alongside Java, and standard resource referencing.

## Example
Let's look at the following helper function definition. Here, the function is defined without annotating the `colorRes` parameter, meaning linting won't work properly.
```kotlin
// Someone's defined a helper without annotating the colorRes parameter - linting fails!
fun resolveColor(colorRes: Int) = requireFragment().resources.getColor(colorRes, null)
```

Here's a simple example of how you might fall in to some trouble using this function with R.java
```kotlin
// A TextView was given a misleading ID, which we mistakenly used
val newColor = resolveColor(R.id.textColor)

// 💥
```

The code above will compile, but when run will crash with a `Resources.NotFoundException`, as the ID we've used doesn't _actually_ refer to a color. We could fix this by adding `@ColorRes` to the `colorRes` parameter, or we could use Res KTX and K.kt. 

Looking again at the same helper but referning to K.kt:

```kotlin
// A TextView was given a misleading ID, which we mistakenly used. This time, we get an error in the IDE
// Error: "Expected type: Int, Found: IdRes"
val newColor = resolveColor(K.id.textColor)

// ✔️
```

### Supported Resource types
All Android resource types are supported, and the generated `K.kt` file should have the same definitions present as your project's `R.java` 

### Extending Resources
As our resource IDs are now types, we can use Kotlin Extension functions to extend functionality when referring to certain resource types. One such extension might be extending `LayoutRes` to make inflating of layouts less verbose:
```kotlin
fun LayoutRes.inflate(parent: ViewGroup, addToRoot = false) = 
    LayoutInflater.from(parent.context).inflate(this.value, parent, addToRoot)

val viewHolder = ItemViewHolder(K.layout.item_view_holder.inflate(parent))
```

This functionality isn't possible using the `R.java` system while leveraging linting, as it's not possible to annotate a reciever type. For example, the following declaration will fail to compile:
```kotlin
@reciever:LayoutRes
fun Int.inflate(/* ... */) // Error: This annotation is not applicable to declaration and use site target
``` 

### Java Interoperability
Kotlin Inline Classes are incompatible with Java, so there is no way of referencing types like `resktx.LayoutRes` from Java, as the types don't exist at runtime. However, if you wanted to make use of `K.kt` in Java code for whatever reason, you'll still get resource linting:
```java
// R.java
setContentView(R.color.red); // Error: Expected resource of type layout

// K.kt
setContentView(K.color.red()); // Error: Expected resource of type layout
```

## How does it work?
Res KTX makes use of the Kotlin's [Inline Classes](https://kotlinlang.org/docs/reference/inline-classes.html) - an experimental feature in 1.3 - to provide type information about what kind of resource an ID refers to while not introducing runtime overhead. A gradle plugin is used to hook into the `R.java` generation, and produce mappings into a `K.kt` file. This mapping is 1:1, with all resource IDs being wrapped in inline class definitions. The runtime component of the library is packaged with extension functions for common framework usecases, 

## Installation
Res KTX is current a work in progress, and is not published anywhere. Watch this space!


