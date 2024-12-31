var extraImagesCount = 0;
dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function () {
    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function () {
        dropdownCategories.empty();
        getCategories();
    })
    getCategories();

    $("input[name='extraImage']").each(function (index) {
        extraImagesCount++;
        $(this).change(function () {
            showExtraImageThumbnail(this, index);
        });
    });
});

function showExtraImageThumbnail(fileInput, index) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };
    reader.readAsDataURL(file);

    if(index >= extraImagesCount - 1){
        addNewExtraImageSection(index + 1);
    }
    
}

function addNewExtraImageSection(index) {
    htmlExtraImage = `
        <div class="col border m-3 p-2" id="divExtraImage${index}">
            <div id="extraImageHeader${index}"><label>Extra Image #${index + 1}:</label></div>
            <div class="m-2">
                <img class="img-fluid" id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" src="${defaultImageThumbnailSrc}" />
            </div>
            <div class="m-2">
                <input type="file" name="extraImage" onchange="showExtraImageThumbnail(this, ${index})" accept="image/png, image/jpeg"/>
            </div>
        </div>
    `;

    htmlLinkRemove = `
        <a class="btn fas fa-times-circle fa-2x icon-dark float-right" 
            href="javascript:removeExtraImage(${index - 1})"
            title="Remove this image"></a>
    `;

    $("#divProductImages").append(htmlExtraImage);

    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
    extraImagesCount++;
}

function removeExtraImage(index){
    $("#divExtraImage" + index).remove();
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