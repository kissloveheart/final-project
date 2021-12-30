
$(document).ready(function(){

    // convert string to number
    function localStringToNumber( s ) {
        return Number( String( s ).replace( /[^0-9,-]+/g, "" ) )
    }

    // remove attribute disabled of modify button
    $("input.ds").change(e => {
        e.preventDefault();
        $("button.modify").prop("disabled", false).attr("id",e.target.id);
    });

    // disable form submissions if there are invalid fields
    $(".needs-validation").submit(function(e){
        // console.log(this);
        // check target budged is below 1000000đ
        if(localStringToNumber($("#targetBudget").val())< 1000000){
            $("#targetBudget")[0].setCustomValidity("Must greater than 1000000");
        } else {
            $("#targetBudget")[0].setCustomValidity("");
        }
        if (!e.target.checkValidity()) {
            e.preventDefault();
            e.stopPropagation();
        }
        $(this).addClass("was-validated");
    });

    // set default date for start date of campaign
    $("#startDateCampaign").val(new Date().toISOString().split('T')[0]);
    $("#endDateCampaign").attr("min",$("#startDateCampaign").val());
    $("#startDateCampaign").change(function (e){
        e.preventDefault();
        $("#endDateCampaign").attr("min", $("#startDateCampaign").val());
    })

    // change target budget to money
    $("#targetBudget").blur(function () {
        let money = $(this).val();
        $(this).val(money? localStringToNumber(money).toLocaleString("vi-VN",{
            currency: "VND",
            style: "currency",
        }): "");
        if(localStringToNumber($("#targetBudget").val()) >= 1000000){
            $("#targetBudget")[0].setCustomValidity("");
        }
    })
    $("#targetBudget").focus(function () {
        let money = $(this).val();
        $(this).val(money ? localStringToNumber(money) : "");


    })
    
    // add form upload image campaign
    $("#addFormUploadImageCampaign").click(function () {
        $("#uploadImageCampaign").append(`<div class="uploadImageDelete">
                         <div class="col-md-9 pb-2">
                             <div class="input-group">
                                 <input type="file" class="form-control" accept="image/*" id="uploadImage" required>
                                 <div class="invalid-feedback">
                                     Vui lòng chọn hình ảnh
                                 </div>
                             </div>
                         </div>

                         <div class="col-md-9">
                             <div class="input-group">
                                 <span class="input-group-text">Mô tả hình ảnh</span>
                                 <input type="text" class="form-control" aria-label="Image descriptions" required>
                                 <div class="invalid-feedback">
                                     Vui lòng nhập mô tả hình ảnh
                                 </div>
                             </div>
                         </div>
                     </div>`).children().last().hide().slideDown(300);
        $("#deleteUploadFormImageCampaign").prop("disabled", false);

    })

    // delete form upload image
    $("#deleteUploadFormImageCampaign").click(function (e) {
       $("#uploadImageCampaign").children().last().slideUp(300, function(){ $(this).remove();});
       if($("#uploadImageCampaign").children().length == 3){
           $("#deleteUploadFormImageCampaign").prop("disabled", true);
       }

    })

    // Reset form create new campaign
    $("#resetFormNewCampaign").click(function () {
        $(".needs-validation").removeClass("was-validated");
    })

});

