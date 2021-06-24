function javaBytes2Hex(arr) {
    var str = "[";
    for (var i = 0; i < arr.length; i++) {
        var z = parseInt(arr[i]);
        if (z < 0) z = 255 + z;
        var tmp = z.toString(16);
        if (tmp.length == 1) {
            tmp = "0" + tmp;
        }
        str = str + " " + tmp;
    }
    return (str + " ]").toUpperCase();
}


function nativeBytes2Hex(bArr) {
    var b = Java.use('[B')
    var buffer = Java.cast(bArr, b);
    var result = Java.array('byte', buffer);
    var str = "[";
    for (var i=0; i < result.length; i++) {
        var z = parseInt(result[i]);
        if (z < 0) z = 255 + z;
        var tmp = z.toString(16);
        if (tmp.length == 1) {
            tmp = "0" + tmp;
        }
        str = str + " " + tmp;
    }
    return (str + " ]").toUpperCase();
}

function hook_java(){


    var clz_sb = Java.use("java.lang.String");
    clz_sb.getBytes.overload().implementation = function(){

        var ret = this.getBytes();
        if(this.indexOf("xxx")!=-1){
            console.log("String#getBytes() method, this = "+this);


            var hex =  javaBytes2Hex(ret);
            console.log("hex = "+hex);


            // var stack = Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Throwable").$new());
            // console.log(stack);

        }
        return ret;
    }

}









function hook_native() {
    console.log("enter hook_native")

    var android_dlopen_ext = Module.findExportByName(null, "android_dlopen_ext");
    console.log(android_dlopen_ext);
    if(android_dlopen_ext != null){
        // console.log("android_dlopen_ext != null  android_dlopen_ext != null");
        Interceptor.attach(android_dlopen_ext,{
            onEnter: function(args){
                // console.log("onEnter")
                var soName = args[0].readCString();
                console.log(soName);
                if(soName.indexOf("libxxx.so") != -1){
                    this.hook = true;
                }
            },
            onLeave: function(retval){
                if(this.hook) {
                    dlopentodo();
                };
            }
        });
    }
    function dlopentodo(){
        
        var libxxx_addr = Module.findBaseAddress("libxxx.so")
        console.log("libxxx_addr is: " + libxxx_addr)


        var sub_1234 = libxxx_addr.add(0x1234+1);
        console.log("sub_1234 is: " + sub_1234)
        Interceptor.attach(sub_1234, {
            onEnter: function (args) {
                console.warn("onEnter sub_1234")

            
                console.warn("sub_1234  args: " + args[0]+",\t"+ Memory.readUtf8String(args[1])+",\t"+ args[2])

                // console.warn("args2 = "+ args[2].readPointer() )
                // console.warn("args2 hexdump = "+ hexdump(args[2].readPointer()) )

                var bArr = nativeBytes2Hex(args[2]);
                console.warn("sub_1234  args[2] , bArr = "+bArr);
            
            },
            onLeave: function (retval) {
                console.warn("sub_1234  Memory.readUtf8String(retval) = "+Memory.readUtf8String(retval))
            }
        })
    }




    
 

   

}


