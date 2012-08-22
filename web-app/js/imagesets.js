
$( function() {
	var retina = (('devicePixelRatio' in window && window.devicePixelRatio > 1) || ('matchMedia' in window && window.matchMedia("(min-resolution:144dpi)").matches));
	var ratioSrc = retina ? (window.devicePixelRatio+'x-src') : "1x-src";

	$('.image-set').each( function(idx, node) {
		var n = $(node);
		var nodeSrc = n.data(ratioSrc);
		if (!nodeSrc) {
			nodeSrc = n.data('default-src');
		}
		n.attr('src', nodeSrc);
	});

});