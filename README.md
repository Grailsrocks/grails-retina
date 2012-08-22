# Retina Images Plugin for Grails

This plugin adds a single tag that is used to hide the horrors of rendering inline images that support high-DPI displays. Which primarily means the 3rd generation iPad and Retina MacBook Pros at the time of writing.

Your site images look like crap on these platforms, especially the larger MacBook Pro if you do not supply high resolution images. Part of the reason for this is that text in web pages looks so incredibly crisp, it just makes the photos and logos look worse.

## What kind of images is this for?

This plugin is just for inline images where you would normally use `<img>` or Grails Resources `<r:img>` tag.

CSS background images are relatively easily provided for using media queries and/or the image-set feature.

What is more problematic is if you want say a clickable logo in your page, or large photos, or other images that are manipulated on the page. These require either some JavaScript to swap images based on client DPI, or to generate CSS on the fly.

This plugin supports both modes.

The challenge lies in not downloading any version of the image until the DPI is known, otherwise your site effectively seems slow and your bandwidth usage will rocket.

## How do I use it?

In any GSP page where you would normally use `<img>` or `<r:img>` and you want to support retina images also, use `img:set`:

	<img:set uri="/images/logo.png" 2x="/images/logo@2x.png" width="100" height="50"/>

You set the `uri` to point at your normal DPI image, and set `2x` to point at your retina image. 

You can add `mode="js"` to force it to use JS to swap the images. This is useful if you are rendering images in a GSP layout after the first `r:layoutResources` has been called. The default mode is 'bg' which uses automatically generated CSS per image, inserted into the `<head>` section of the page.
