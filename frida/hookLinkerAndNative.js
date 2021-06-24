
hook_native()
// hooklinker()


function log_msg(msg) {
    var Log = Java.use("android.util.Log");

    var TAG_L = "xyz";

    Log.v(TAG_L, msg);
}


function hook_native() {
    console.log("enter hook_native")


    var android_dlopen_ext = Module.findExportByName(null, "android_dlopen_ext");
    // console.log(android_dlopen_ext);
    if (android_dlopen_ext != null) {
        Interceptor.attach(android_dlopen_ext, {
            onEnter: function (args) {
                // console.log("onEnter")
                var soName = args[0].readCString();
                console.log("soName : " + soName);

                if (soName.indexOf("libmain.so") != -1) {  
        
                    this.hook = true;
                }
            },
            onLeave: function (retval) {
                if (this.hook) {
                    dlopentodo();
                };
            }
        });
    }

    function dlopentodo() {

        var libmain_addr = Module.findBaseAddress("libmain.so")
        console.warn("libmain_addr is: " + libmain_addr)

    }

}



// 通过linker去hook init_array
var hasHook = false;
function hooklinker() {
    var linkername = "linker";
    var find_libraries_addr = null;
    var arch = Process.arch;
    LogPrint("Process run in:" + arch);
    if (arch.endsWith("arm")) {
        linkername = "linker";
    } else {
        linkername = "linker64";
        LogPrint("arm64 is not supported yet!");
    }

    var symbols = Module.enumerateSymbolsSync(linkername);
    for (var i = 0; i < symbols.length; i++) {
        var symbol = symbols[i];
        // LogPrint(linkername + "->" + symbol.name + "---" + symbol.address);
        // if (symbol.name.indexOf("__dl__ZL13call_functionPKcPFviPPcS2_ES0_") != -1) {
        if (symbol.name.indexOf("__dl__Z14find_librariesP19android_namespace_tP6soinfoPKPKcjPS2_PNSt3__16vectorIS2_NS8_9allocatorIS2_EEEEjiPK17android_dlextinfobbPNS9_IS0_NSA_IS0_EEEE") != -1) {
            find_libraries_addr = symbol.address;
            LogPrint("linker->" + symbol.name + "---" + symbol.address)


            var so_name = null;

            // void *find_libraries_hook(void* ns, void* start_with, const char* const library_names[], 
            // size_t library_names_count, void* soinfos, 
            // void* ld_preloads, void* ld_preloads_count, 
            // int rtld_flags, void* extinfo, bool add_as_children, 
            // bool search_linked_namespaces, void* namespaces)
            Interceptor.attach(find_libraries_addr, {
                onEnter: function (args) {
                    // console.log("onEnter find_libraries_addr")
                    // console.log(JSON.stringify(args[2]));
                    // console.log(Memory.readCString(args[2]));

                    // console.log("当前加载的 So 个数：" + args[3]);

                    for (var i = 0; i < args[3]; ++i) {
                        var base_name_addr = args[2];
                        var sub_name_addr = args[2].add(i * 4).readPointer();
                        so_name = sub_name_addr.readCString()
                        // console.log("so_name = " + so_name);
                        // console.log("--------------------------------------------------------------");
                    }


                },
                onLeave: function (retval) {
                    //     // console.log("onLeave find_libraries_addr  retval:", retval);

                    if (so_name.indexOf("libmain.so") != -1) {


                        if (hasHook == true) {
                            console.log("非首次进入，不用再hook");
                            return;
                        }
                        hasHook = true;


                        var libmain_addr = Module.findBaseAddress(so_name)
                        console.log("libmain_addr = " + libmain_addr)


                

                        var pthead_create_check_addr = libmain_addr.add(0x1234 + 1); 
                        console.log("pthead_create_check_addr is: " + pthead_create_check_addr)
                   
                        //   int __fastcall sub_1234(int a1, int a2, int a3, int a4)
                        var pthead_create_check_function = new NativeFunction(pthead_create_check_addr, 'int', ['int','int','int','int']);
                        Interceptor.replace(pthead_create_check_function, new NativeCallback(function () {
                            console.log("enter pthead_create_check_function hook function");
                            return 0; /// 直接返回
                        }, 'int', ['int','int','int','int']))

                    }




                }
            })


        }
    }


}