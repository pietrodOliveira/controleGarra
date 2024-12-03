function addCampo(){
    $.ajax({
        url: "/getPv",
        method: "post",
        success: function(response){
            let div = $(response).addClass('campo');
            $(".row").append(div);
        },
        error: function(){
            alert("Fodeu ao adicionar");
        }
    })
}
$("#addBtn").click(addCampo);

function removeCampo(){
    $.ajax({
        url: "/getPv",
        method: "post",
        success: function(response){
            $(".row .campo").last().remove();
        },
        error: function(){
            alert("Fodeu ao remover");
        }
    })
}
$("#removeBtn").click(removeCampo);

function resetarBraco(){
    $("#resetBtn").attr("disabled", true);
    $("#botaoSubmitDados").attr("disabled", true);

    $.ajax({
        url: "/reset",
        method: "post",
        success: function(response){
            console.log("Reset enviado", response);
            $("#resetBtn").removeAttr("disabled");
            $("#botaoSubmitDados").removeAttr("disabled");
        },
        error: function(){
            console.log("Erro", error);
            $("#resetBtn").removeAttr("disabled");
            $("#botaoSubmitDados").removeAttr("disabled");
        }
    })
}
$("#resetBtn").click(resetarBraco);

function reconnect(){
    $("#reconnectBtn").attr("disabled", true);
    $("#loading").removeClass("d-none");
    $("#disconnected").addClass("d-none");
    $("#check").addClass("d-none");

    $.ajax({
        url: "/reconnect",
        method: "post",
        success: function(response){
            if(response == 1){
                $("#loading").addClass("d-none");
                $("#check").removeClass("d-none");
                $("#disconnected").addClass("d-none");
                $("#reconnectBtn").removeAttr("disabled");
            }else{
                $("#loading").addClass("d-none");
                $("#check").addClass("d-none");
                $("#disconnected").removeClass("d-none");
                $("#reconnectBtn").removeAttr("disabled");
            }
        },
        error: function(xhr, status, error){
            console.log("Erro ao tentar reconectar", status, error);
            $("#disconnected").removeClass("d-none");
            $("#reconnectBtn").removeAttr("disabled");
        }
    });
}
$("#reconnectBtn").click(reconnect);

$('#botaoSubmitDados').on('click', function(e) {
    $("#botaoSubmitDados").attr("disabled", true);
    $("#resetBtn").attr("disabled", true);
    e.preventDefault(); // Evita o envio normal do formulário

    // Cria um array ou objeto para armazenar os dados a serem enviados
    var dados = [];

    // Para cada item de formulário dentro do contêiner
    $('.input-group').each(function() {
        // Captura os valores do select e do input
        var motorValue = $(this).find('select[name="servo"]').val();
        var anguloValue = $(this).find('input[name="anguloServo"]').val();

        // Adiciona os dados a um array (ou objeto)
        dados.push({
            motor: motorValue,
            angulo: anguloValue
        });
    });

    $.ajax({
         url: '/garra',  // Caminho correto para o seu controller
         type: 'POST',
         contentType: 'application/json',  // Especificando o tipo de conteúdo como JSON
         data: JSON.stringify(dados),  // Envia os dados como uma string JSON
         success: function(response) {
            console.log('Resposta do servidor: ', response);
            $("#botaoSubmitDados").removeAttr("disabled");
            $("#resetBtn").removeAttr("disabled");
         },
         error: function(xhr, status, error) {
            console.error('Erro ao enviar dados: ', error);
            $("#botaoSubmitDados").removeAttr("disabled");
            $("#resetBtn").removeAttr("disabled");
         }
    });
});