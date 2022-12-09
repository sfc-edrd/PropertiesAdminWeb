document.getElementsByClassName("btn-danger")[0].onclick = function()
{
    if (window.confirm("This action will discard all changes. Are you sure?"))
    {
        window.location = window.location.origin + window.location.pathname;
    }
};

function removeRow(event)
{
    var srcElementParentName;

    srcElementParentName = event.srcElement.parentElement.name;
    document.getElementById(srcElementParentName).remove();
}

function sendData()
{
    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8080/properties-admin.html";

    xhr.open("GET", url);
    xhr.onreadystatechange = () => {receivedResponse(xhr)};
    xhr.send();
}
