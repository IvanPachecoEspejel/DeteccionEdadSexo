/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$('#submit').click(function() {
    var fileName = $("#fileUpload").val();
    if (!fileName) {
        alert("Please select a file to upload");
    }
});