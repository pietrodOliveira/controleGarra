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
    $.ajax({
        url: "/reset",
        method: "post",
        success: function(response){
            console.log("Reset enviado", response);
        },
        error: function(){
            console.log("Erro", error);
        }
    })
}
$("#resetBtn").click(resetarBraco);

$('#botaoSubmitDados').on('click', function(e) {
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
         },
         error: function(xhr, status, error) {
            console.error('Erro ao enviar dados: ', error);

         }
    });
});