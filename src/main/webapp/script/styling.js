/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#formImage')[0].reset();
});

$(document).ready(function () {
    $('#submit').click(function () {
        var fileName = $("#fileUpload").val();

        if (!fileName) {
            alert("Please select a file to upload");

            return;
        }
        
        $.ajax({
            url: 'imageupload',
            type: 'POST',
            data: new FormData($('#formImage')[0]),
            processData: false,
            contentType: false,
            success: function (data) {
                $('#resultado').html(data);
            }
        });
    });
});