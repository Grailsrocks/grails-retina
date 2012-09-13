# Retina Images Plugin for Grails

This plugin adds a single tag that is used to hide the horrors of rendering inline images that support high-DPI displays. Which primarily means the 3rd generation iPad and Retina MacBook Pros at the time of writing.

Your site images look like crap on these platforms, especially the Retina MacBook Pro if you do not supply high resolution images. Part of the reason for this is that text in web pages looks so incredibly crisp, it just makes the photos and logos look worse.

*This implementation is simply a first stab. It works as a quick fix. More ideas and approaches are in the pipeline.*

## What kind of images is this for?

This plugin is just for inline images where you would normally use `<img>` or Grails Resources `<r:img>` tag.

This is not for your CSS background images such as textures and maybe logos. Think photos, diagrams, avatars etc. CSS background images are relatively easy to implement yourself directly in CSS using media queries and/or the image-set feature.

It is more problematic if you want say a clickable logo in your page, or large photos, or other images that are manipulated on the page. These require either some JavaScript to swap images based on client DPI, or to generate CSS on the fly.

This plugin supports both modes, as both have their merits.

The challenge lies in not downloading any version of the image until the DPI is known, otherwise your site effectively seems slow and your bandwidth usage will rocket because people with retina displays download both the normal and high-resolution versions.

## How do I use it?

In any GSP page where you would normally use `<img>` or `<r:img>` and you want to support retina images also, use `img:set`:

	<img:set uri="/images/logo.png" 2x="/images/logo@2x.png" width="100" height="50"/>

You set the `uri` to point at your normal DPI image, and set `2x` to point at your retina image. 

You can add `mode="js"` to force it to use JS to swap the images. This is useful if you are rendering images in a GSP layout after the first `r:layoutResources` has been called. The default mode is 'bg' which uses automatically generated CSS per image, inserted into the `<head>` section of the page.

## CSS 'bg' mode

This is the default, and uses `background-image` along with the `image-set` function supported in latest Safari and Chrome browsers. It generates CSS specific to the image which is gathered together and appended to `<head>` as a single `<style>` block in the page.

The benefits of this are that the image to load is known as the page loads. No missing images until page load is complete.

Also, if the user has multiple displays, dragging the browser between retina and non-retina displays does the right thing - it shifts to the correct resolution image for that display.

However as this uses Resources plugin's new `<r:stash>` tag to specify inline styles that get rendered in `<head>`, it means that you can only use this mode if the `<head>` section of the final output has not already been rendered.

In short this means you cannot use this mode after the first `<r:layoutResource>` tag has executed in your layout.

## Scripted 'js' mode

This mode renders `<img>` tags with empty `src` and once the page loads, swaps in the correct URLs into the `src` attribute. It works for images used anywhere in your Sitemesh layout, but no version of the image can load until the JS executes at the end of the page.

This means you get a "FOUC" where photos do not appear at all until the JS executes after loading all your other JS.

## Known issues

* No support yet for placeholder images
* CSS "bg" mode does not use an `<img>` so it is not semantic
* Not sure how expressing fractional pixel ratios as attributes will work... but not that bothered sorry Androiders and Windows Phonies.
* Extra attributes passed to image may not output currently
* We should probably auto-sense whether to use JS or CSS based on what dispositions remain for rendering