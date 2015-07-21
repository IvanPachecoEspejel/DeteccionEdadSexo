/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


if (window.File && window.FileReader && window.FileList && window.Blob) {
  // Great success! All the File APIs are supported.
} else {
  alert('The File APIs are not fully supported in this browser.');
}


$(document).ready(
    function() {
        $("#fileUpload").change(
            function(evt) {
                $("#fileName").html($(this).val()); 
                
                var file = evt.target.files[0]; // FileList object

                // files is a FileList of File objects. List some properties.
                var reader = new FileReader();

                  // Closure to capture the file information.
                  reader.onload = (function(theFile) {
                    return function(e) {
                      // Render thumbnail.
                      document.getElementById('show').innerHTML = ['<img class="thumb" src="', e.target.result,
                                        '" title="', escape(theFile.name), '"/>'].join('');
                    };
                  })(file);

                  // Read in the image file as a data URL.
                  reader.readAsDataURL(file);  
            }
        );
    }
);
