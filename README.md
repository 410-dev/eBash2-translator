# eBash2 to Native Translator
This tool will let developers to translate from eBash2 to the native bash.


### Reasons to translate to native
There are two main reasons that eBash2 script must be translated into native bash.

1. Users who are using the script must have eBash2 installed to their computer, or must run the temporary installation script which takes a lot of time and effort.
2. As the scripts refers to each other, the number of time running "@import" will dramatically slows down the overall performance of the instance. 


### Solution
The translator will analyze the imported packages recursively, and will bring the entire functions to script. Then, the scripted functions will be inserted in the header of the script. This will trick the original script that the functions are already loaded, which means that eBash2 runtime and interpreter is no longer necessary.
Since it is nearly a migrating the native code, it is basically internalizing the libraries that is written in native bash so that the script can be independent from eBash2 runtime and interpreter.


### Usage
```bash
java --enable-preview -jar eb2native.jar -i <input file/directory path>
```

Optional arguments:
```
-o / --output:   Specify the output location.
-r / --runtime:  Specify the custom eBash2 runtime.
```


