$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        // Codigo funciona con thymeleaf, pero al estar en common se usa JS
        // window.location = "[[@{/users}]]";

        // Se debe declarar esta variable en las pag donde se utiliza la function
        window.location = moduleURL;
    });

    $("#fileImage").change(function () {
        fileSize = this.files[0].size;
        // alert("File size: " + fileSize);

        if (fileSize > 102400) {
            this.setCustomValidity("You must choose an image less than 100KB!");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }

        showImageThumbnail(this);
    })
});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

// Se llama desde user_form y category_form
function showErrorModal(message){
    showModalDialog("Error", message);
}

function showWarningModal(message){
    showModalDialog("Warning", message);
}
