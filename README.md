PDF-web-API
===========
This application is a dedicated server, used for automatic acrofields population of pdf files.
This api is built using sparkjava - lightweight web-server and iText pdf manipulation software.

If you only need a pdf manipulation software, see iText.

This application should be used with other software by communicating through the http protocol
and receiving the pdf file as response.

Installation
============
Make sure java-8 and maven is installed on the system. Then execute:
```
> git clone https://github.com/Gronis/pdf-web-api.git
> mvn package
```

Usage
=====

Start server
```
java -jar pdf-web-api.jar
```
This api level currently doesn't support upload of pdf template file to use. Once a pdf file is placed in the template folder on the server, call:

```
http://url:port/generate/pdf-file-name.pdf?acrofield1=sometext&acrofield2=someothertext etc...
```

There is a test file you can run with if you start the application. Start the server on localhost and call
```
http://localhost:1337/generate/test.pdf&Message=World
```
You should see a "Hello World" message

