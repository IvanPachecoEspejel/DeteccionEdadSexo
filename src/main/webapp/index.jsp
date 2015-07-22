<%-- 
    Document   : index
    Created on : 16/07/2015, 08:48:22 PM
    Author     : ivan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        
        <script type="text/javascript" src="script/jquery-2.1.4.min.js"></script>
        <script type="text/javascript" src="script/images.js"></script>
        <script type="text/javascript" src="script/styling.js"></script>
        
        <link type="text/css" rel="stylesheet" href="css/style.css" />
       
        <title>IBHI</title>
    </head>
    <body>
        <header>
            <h1>Detecci&oacute;n de <span class="italic">Sexo</span> y <span class="italic">Edad</span></h1>
        </header>
        <div id="principal">
            <form method="post" enctype="multipart/form-data">
                <h2>Elige tu foto para clasificar:</h2>
                <div id="buttons">
                    <div id="upload">
                        <input type="button" id="fileButton" class="uploadButton" value="Browse" />
                        <input type="file" id="fileUpload" name="imagen" accept="image/*" />
                        <span id="fileName">Select file...</span>
                    </div>
                    <input type="button" id="submit" class="uploadButton" name="clasficar" value="Clasificar"/>
                </div>
                <div id="thumb">
                    <output id="show"></output>
                </div>
            </form>
        </div>
    </body>
</html>
