import { Avatar, Backdrop, Box, Button, CircularProgress, Container, createTheme, CssBaseline, Grid, TextField, ThemeProvider, Typography } from '@mui/material';
import React, { useState } from 'react'
import { Link } from "@mui/material";
import {  useNavigate } from 'react-router-dom';
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { login } from '../../../services/auth/Auth';
import { useSnackbar } from 'notistack';
import { saveToken } from '../../../utility/Common';

const defaultTheme = createTheme();

const Login = () => {
  const { enqueueSnackbar } = useSnackbar();
  const[formData , setFormData] = useState({
    email:'',
    password:''
  });
  const [loading , setLoading] = useState(false);
  const navigate = useNavigate();

  const handleInputChange = async(e)=>{
    //name : email value is like : abc
    const name = e.target.name;   // "email" or "password"
    const value = e.target.value;    // what user typed
    setFormData({
      ...formData,
      [name] : value  //array bcz name has email and pass
    })
  };


const handleSubmit = async (e) => {
  e.preventDefault();
  setLoading(true);

  try {
    //response stores what backend sent back
      const response = await login(formData);  //in response we get status and data jwtToken ,name
      if (response.status === 200) { //OK

          const responseData = response.data;
          saveToken(responseData.jwtToken);
          navigate("/dashboard");
          enqueueSnackbar(`Welcome ${responseData.name}`, { variant: 'success', autoHideDuration: 5000 });
      }
  } catch (error) {
      
    enqueueSnackbar('Sign in failed!', { variant: 'error', autoHideDuration: 5000 });
      
  } finally {
      setLoading(false);
  }
};


return (
  <>
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
          <CssBaseline/>
          <Box
            sx={{
              marginTop : 8,
              display:"flex",
              flexDirection:'column',
              alignItems:'center'
            }}
          >
              <Avatar sx={{ m: 1, bgcolor: "primary.main" }}>
                <LockOutlinedIcon/>
              </Avatar>
              <Typography variant="h5" component="h1">
                Sign In
            </Typography>
              <Box component="form" onSubmit={handleSubmit}
              noValidate sx={{mt : 1}}>
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  value={formData.email}
                  onChange={handleInputChange}
                />
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  id="password"
                  label="Password"
                  name="password"
                  autoComplete="password"
                  autoFocus
                  value={formData.password}
                  onChange={handleInputChange}
                />
                <Button
                  type='submit'
                  fullWidth
                  variant='contained'
                  sx={{mt : 3 , mb : 2}}
                  disabled = {!formData.email || !formData.password}
                >
                  {loading ? <CircularProgress color="success" size={24}/> : "Sign In"}
                </Button>

                <Grid container>
                  <Grid>
                    <Link variant="body2" onClick={()=>navigate("/register")}>
                    {"Don't have an account ? Sign Up"}
                    </Link>
                  </Grid>
                </Grid>

              </Box>
          </Box>
        </Container>

    </ThemeProvider>
    <Backdrop
      sx={{color : "#fff" , zIndex : (theme) => 
        theme.zIndex.drawer + 1}}
      open={loading}
      >
        <CircularProgress color = "success" />
    </Backdrop>
  </>
)
}

export default Login
