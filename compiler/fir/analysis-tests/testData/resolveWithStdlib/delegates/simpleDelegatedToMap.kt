// RUN_PIPELINE_TILL: BACKEND
class C(val map: MutableMap<String, Any>) {
    var foo by map
}

var bar by hashMapOf<String, Any>()