
$(document).ready(function(){
    // remove attribute disabled of modify button
    $("input.ds").change(e => {
        e.preventDefault();
        $("button.modify").prop("disabled", false).attr("id",e.target.id);
    });

});

// disable form submissions if there are invalid fields
$(".needs-validation").submit(e =>{
    e.preventDefault();
    console.log(this);
    if (!e.target.checkValidity()) {
        console.log("check");
        e.preventDefault();
        e.stopPropagation();
    }
    this.addClass("was-validated");
});
