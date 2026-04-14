import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";

const TOKEN = 'token';
// 'token' is just the KEY NAME used to store in cookie
// like a label on a jar

export const saveToken = (token) =>{
    Cookies.set(TOKEN , token);
}

// Cookies.set('token', 'eyJhbGci...')
//              ↑           ↑
//           key name    actual JWT value
//          (label)      (content)

// This saves the JWT token received after login into the browser cookie with the key name "token".

export const getToken = ()=>{
    return Cookies.get(TOKEN) || null;
}

export const removeToken = ()=>{
    Cookies.remove(TOKEN);
}

export const decodeToken = ()=>{
    const token = getToken();
    if(!token) return null;
    try{
        return jwtDecode(token);
         // decodes "eyJhbGciOiJIUzI1NiJ9..."
        // returns → {
        //     sub: "john@example.com",
        //     id: 1,
        //     role: "USER",
        //     exp: 1234567890
        // }
    }
    catch(error)
    {
        console.log('Failed to decode token:',error);
        return null;
    }
}

export const isTokenValid = ()=>{
    const decodedToken = decodeToken(); //Used decoded token function
    //Ensure token exist and has an expiry
    if(!decodedToken || !decodedToken.exp) return false;

    const expiry = decodedToken.exp * 1000;  //convert expiry to milliseconds
    return Date.now() < expiry;  //check if token is still valid

    // If current time < expiry → token still valid → true
    // If current time > expiry → token expired    → false
}