# Muse - A Dysfunctional Language

Created by Andrew Zuo, ([GitLab](https://gitlab.com/azuredown), [This-Other-Incredibly-Overrated-Git-Host-That-Used-To-Suck-Until-Microsoft-Bought-It](https://github.com/impure)).

Code is available [here](https://gitlab.com/azuredown/muse), or you can pull the CWF repo and use the commands `git submodule init` and `git submodule update`.

## Inspiration

![And when everything is a function...](https://gitlab.com/azuredown/muse/-/raw/master/Images/When%20Everything%20Is%20A%20Function.jpg)

I finally got tired of those snobbish functional programmers and was like, "Oh, yeah? What if I made a language without functions? A dysfunctional language." And then I was like, "Woah, dude."

But what really got me thinking about this is C\# which is really similar to Java (think of it as Microsoft's non-trademark-infringing version of Java). It's easy to use, but it also has so much baggage from Java. There's just so much typing. That is keyboard typing not type typing.

## Summary

![This is what it looks like](https://i.imgur.com/jBf8URr.png)

So first of all the language technically has functions. The thing that makes it dysfunctional is that any function can be instantiated (although this has changed to only void functions can be instantiated). So every function is a class... Or every class is a function. Or perhaps it's more correct to say there are neither *classes* nor *functions*, only *containers*.

Syntax wise it's very similar to Python because Python is simple. But I also made sure to add some custom flourishes. First of all you can start a variable name with a number. I always wanted to do that. Also when accessing a variable in a parent container, a 'global' variable, you have to use unix directory syntax like ../var1. Because why oh why do modern languages use the same syntax to access global and local variables.

Also there's [this license](https://gitlab.com/azuredown/muse/-/blob/master/LICENSE) I wrote for the project. I really like it. It's based off of MIT with two additional clauses that I really like.

## Challenges

The biggest challenge by far was the Stack Map. I almost gave up because of this. Basically JVM bytecode must have an attribute called a 'Stack Map Table'. This is so when you jump to a location such as in a while loop the Java Runtime will know what variables have already been defined at that point in the code. Now if you don't have a Stack Map your code can technically run using the `-noverify` flag although you will get a warning message that this flag will be removed in a future version of Java. Presumably the Stack Map is for optimization purposes only.

This was so challenging because the [documentation](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.4) on the Stack Map Table is so confusing. And the layout of the table does not resemble the JVM instructions like at all. But eventually after [sleeping on it I figured it out](https://youtu.be/tQ7_vbNEDEk?list=PLe_b-HAZD1pW-Da_Atlx_1XfyNVe-sv9r&t=90). The Stack Map doesn't contain all the information in a single entry. It contains information relative to the last stack map entry. Sort of like predicted frames (P-frames) in videos.

Apart from this I didn't have many problems. Mostly due to the various tools for analyzing JVM bytecode and documentation.

## Tools And Resources

Without a doubt the most useful resource was Oracle's documentation on the JVM class file format. Most notably the actual assembly instructions found [here](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html) and the class file specification found [here](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.4). There's some other resources but I mostly just used those two.

Then there's the tool for viewing the class file: `javap`. Although the way to properly run it is `javap -v CLASS_FILE_NAME`. This provides a readout of exactly what is in your class file. It's pretty useful for debugging things like a jump instruction that is pointing to the wrong location or if you run out of stack space.

![Javap](https://i.imgur.com/KMohXlk.png)

Although it's not perfect so I did occasionally need to view the file in a hex editor. I didn't have to do this too often though.

Also I did find [this resource](https://medium.com/@davethomas_9528/writing-hello-world-in-java-byte-code-34f75428e0ad) on creating a hello world program in JVM bytecode helpful, but only at the start. There's also some other resources listed [here](https://gitlab.com/azuredown/muse/-/blob/master/Notes/Other%20Notes.txt), although I didn't find any of the other ones particularly useful.

## What Would You Have done Differently?

I would have used a parser other than ANTLR. ANTLR is just terrible. It has like no debugging output and it just gets so confused by the weirdest things. The best thing I could find for it is this IntelliJ ANTLR plugin although the plugin apparently caused IntelliJ to crash occasionally when it was open.

And ANTLR is buggy too. Every time something went wrong I had to check if it was actually ANTLR causing problems because it got confused between an assignment and a print again. Plus all the tutorials for it are complete garbage.

Ideally I would write my own parser although this might take an unreasonably high amount of time. Or maybe not. It doesn't sound too hard. All you have to do is transform the string to tokens and then you transform the tokens into a syntax tree. I bet I could write one that was way better than ANTLR. And more flexible too. And I could have tokens like + - / * without needing to have white space between them like ANTLR.

Also I don't really like the JVM. Yeah, it's easier, but you can tell the JVM was designed for Java. It was not designed for custom languages and definitely not one with Python-style syntax.

I would use LLVM because that's what the big kids use. Big kids as in Apple, Oracle, Mozilla, and Microsoft. I originally tried using LLVM but it wasn't easy to set up and eventually I just decided the JVM was easier.

