import axios from "axios";
import {getToken} from '../utility/Common.jsx';

const instance = axios.create({
    baseURL : 'http://localhost:8080/',
})

//Add a request interceptor
instance.interceptors.request.use(
    (config) =>{
        // config = request details
    // { url, method, headers, data... }
        const token = getToken();

        if(token) // if token exists → add to header
        {
            
            config.headers.Authorization = `Bearer ${token}`;
            
        }
        return config;
    },
    (error) =>{
        return Promise.reject(error);
    }
);

export default instance;






// This creates a pre-configured axios instance with your backend URL as the base. So instead of writing the full URL every time:
// javascript// ❌ Without instance - repeat URL everywhere
// axios.post('http://localhost:8080/api/auth/login', data)
// axios.get('http://localhost:8080/api/user/profile')

// // ✅ With instance - just write the endpoint
// instance.post('api/auth/login', data)
// instance.get('api/user/profile')