dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function () {

    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function () {
        dropdownCategories.empty();
        getCategories();
    })

    getCategoriesForNewForm();

});

function getCategoriesForNewForm(){
    catIdField = $("#categoryId"); // Para ver si es en new o edit mode
    editMode = false;

    if(catIdField.length){
        editMode = true;
    }

    if(!editMode) getCategories();
}


function getCategories() {
    brandId = dropdownBrands.val();
    url = brandModuleURL + "/" + brandId + "/categories";

    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        })
    });

}

function checkUnique(form) {
    var productId = $("#id").val();
    var productName = $("#name").val();
    var csrfValue = $("input[name='_csrf']").val();

    // Deben hacer match con los parametros que recibe el controlador
    var params = { id: productId, name: productName, _csrf: csrfValue };

    // checkUniqueurl viene del product_form
    $.post(checkUniqueurl, params, function (response) {
        if (response == "OK") {
            form.submit();
        } else if (response == "Duplicate") {
            showWarningModal("There is another product having the same name " + productName);
        } else {
            showErrorModal("Unknown response from server");
        }
    }).fail(function () {
        showErrorModal("Could not connect to the server");
    })

    return false;
}