BitServer

Our very simple HTTP server.

Today we worked on a simple HTTP server. The point was to deal with invalid requests and log the app behavior. To to this we divided the code and used the BitLoger writen the day before.

Tasks:

Use an array of allowed extensions instead of just checking if the requested file name contains a dot (.)
Move the css and js files into a seperate folder called assets. The assets folder should contain 2 other folders "css" and "js" so all .css files should be inside ./assets/css and all .js files inside ./assets/js
Since our .js and .css files are now in a different folder you should change the basePath (inside the getFileName() function) so that it is constructed depending on the requested file's extension
Add logging of requests and which response has been served (ok/error/serverError)
