modules = {
    'plugin.retina.imagesets' {
        dependsOn 'jquery'
        
        resource url:[plugin:'retina', dir:'js', file:'imagesets.js']
    }
}