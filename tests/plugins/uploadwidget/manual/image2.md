@bender-tags: 4.5.0, tc, clipboard, widget, filetools
@bender-ui: collapsed
@bender-ckeditor-plugins: wysiwygarea, toolbar, undo, uploadwidget, basicstyles, image2, uploadimage, font, stylescombo, basicstyles, format, maximize, blockquote, list, table, resize, elementspath, justify
@bender-include: _helpers/xhr.js

Test if photo uploading works properly:

* Dropped image should be replaced by a temporary `upload widget` and by the final image (`image2` plugin) when upload is dome.
* `jpg`, `png` and `gif` files should be supported.
* Undo and redo during and after upload should work fine.
* It should be possible to format text and copy image during upload.
* If image is removed during upload, the process should be aborted.
* Check if you see a message about the progress, success and abort.

**Note:** This test use upload mock which will show you *Lena* instead of the real uploaded image.