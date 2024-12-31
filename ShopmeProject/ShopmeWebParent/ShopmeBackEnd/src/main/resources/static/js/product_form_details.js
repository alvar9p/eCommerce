function addNextDetailSection() {
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    htmlDetailSection = `
        <div class="form-inline" id=divDetail${divDetailsCount}>
            <label class="m-3">Name:</label>
            <input class="form-control w-25" type="text" name="detailNames" maxlength="255" />
            <label class="m-3">Value:</label>
            <input class="form-control w-25" type="text" name="detailValues" maxlength="255" />
        </div>
    `;

    $("#divProductDetails").append(htmlDetailSection);

    previousDivDetailSection = allDivDetails.last();
    previousDivDetailID = previousDivDetailSection.attr("id");

    htmlLinkRemove = `<a class="btn fas fa-times-circle fa-2x icon-dark" 
        href="javascript:removeDetailSectionById('${previousDivDetailID}')" title="Remove this detail"></a>`;

    previousDivDetailSection.append(htmlLinkRemove);

    $("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id){
    $("#" + id).remove();
}