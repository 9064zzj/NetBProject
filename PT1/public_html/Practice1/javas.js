/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function click() {
    var username = document.getElementById('Username').value; 
    var email = document.getElementById('Email').value; 
    var password1 = document.getElementById('Password').value; 
    var password2 = document.getElementById('RePassword').value; 
    
    if(username == ""|| email == ""||password1 == ""|| password2 == ""){
        alert ('Empty value is not allowed');
        
    }
    else{
        if(email.indexOf("@")==-1){
            alert ('Please input a valid email');
        }
        else{
            if(password1.value == password2.value){
                alert ('Congratulations! You sumbit successfully!');
            }
            else{
                alert ("Your passwords are not match!");
            }
        }
    }
}
