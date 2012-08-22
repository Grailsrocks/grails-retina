/* Copyright the original author or authors:
 *
 * 		Marc Palmer (marc@grailsrocks.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grailsrocks.plugin.retina

import org.grails.plugin.platform.util.TagLibUtils

class RetinaTagLib {
	static namespace = 'img'

	// Options:
	// 1) CSS downsampling - bad, everyone gets hires image
	// 2) HTML with JS replacement - generate no link on page load, JS substitutes correct url in. No images until page loaded
	// 3) CSS image-set - BG images only/span tricks needed to be clickable, dynamic CSS needed
    // 4) CSS with media-queries - would adapt in realtime but requires knowledge of the client PPIs in advance
	def set = { attrs ->
		def defaultSrc = r.resource(uri:attrs.remove('uri'))
		def w = attrs.remove('width')
		def h = attrs.remove('height')
		def alt = attrs.remove('alt')
		def mode = attrs.remove('mode') ?: 'bg'
		switch (mode) {
			// This handles realtime changes to dpi i.e. dragging browser between retina and non-retina displays, loads immediately
			// Cannot be called after r.layourResources for head has been executed
			// Although we *could* bypass this by writing out inline styles on the span, or a <style> block just before
			// ideally however these would be in an external file generated only once
			case 'bg':
				def id = TagLibUtils.newUniqueId(request)
				out << "<span id=\"imageset${id.encodeAsHTML()}\"/>"
				def imgsets = new StringBuilder()
				imgsets <<= "url('${defaultSrc}') 1x"
				for (e in attrs) {
					def url = r.resource(uri:e.value)
					imgsets <<= ", url('${url}') ${e.key}"
				}
				r.stash(disposition:'head', type:'style') {
					"""
					#imageset${id} {
						padding: 0;
						width: ${w}px;
						height: ${h}px;
						display: block;
						background-size: ${w}px ${h}px;
						background-image: url(${defaultSrc});
						background-image: -webkit-image-set($imgsets);
						background-image: -moz-image-set($imgsets);
						background-image: -o-image-set($imgsets);
						background-image: -ms-image-set($imgsets);
					}
"""
				}
				break;
			// This does not handle realtime changes to dpi i.e. dragging browser between retina and non-retina displays, loads only when page is ready
			// However it can be used no matter where in the page/processing the image occurs, and it is semantically correct
			case 'js':
			default:
				// @todo output all other user-supplied attributes, and support a placeholder image
				out << "<img src=\"\" width=\"${w}\" height=\"${h}\" class=\"image-set\" data-default-src=\"${defaultSrc.encodeAsHTML()}\""
				def imgsets = new StringBuilder()
				for (e in attrs) {
					def url = r.resource(uri:e.value)
					imgsets <<= " data-${e.key}-src=\"${url.encodeAsHTML()}\""
				}
				out << imgsets
				out << "/>"
				r.require(module:'plugin.retina.imagesets')
				break;
		}
	}
}