# Spring MVC Image Upload

This project is a demonstration of how to upload images in Spring MVC and then serve those images back to users.

The same techniques could be used to facilitate uploading and downloading files of any type.

## Files Involved

* `src/main/resources/application.properties`
  * This file sets the maximum upload filesize.
  * By default the max upload size is relatively small, so this will likely need adjustment.
* `src/main/resources/templates/index.html`
  * Provides the form to upload a file.
  * Currently accepts a file and a name for the file.
  * Displays a message if a file was uploaded successfully.
* `src/main/java/com/example/imageupload/ImageUploadController.java`
  * Displays the index.html page, which contains the file upload form.
  * Accepts file uploads and saves them to a specific directory. Currently does nothing with the additional file name from the form. This could be stored in a database or used to save the file under the provided name.
  * Serves previously uploaded files to the user.
