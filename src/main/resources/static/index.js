function addCampo(){
    $.ajax({
        url: "/getPv",
        method: "post",
        success: function(response){
            $(".row").append(response);
        },
        error: function(){
            alert("Fodeu");
        }
    })
}
$("#addBtn").click(addCampo);