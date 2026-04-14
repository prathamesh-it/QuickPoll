import { Avatar, Backdrop, Box, Button, CircularProgress, Container, createTheme, CssBaseline, Grid, TextField, ThemeProvider, Typography } from '@mui/material';
import React, { useState } from 'react'
import { Link } from "@mui/material";
import {  useNavigate } from 'react-router-dom';
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { useSnackbar } from 'notistack';
import { signup } from '../../../services/auth/Auth';
import { saveToken } from '../../../utility/Common';

const defaultTheme = createTheme();

const Signup = () => {
const { enqueueSnackbar } = useSnackbar();
const[formData , setFormData] = useState({
    email:'',
    password:'',
    firstName : '',
    lastName : ''
});
const [loading , setLoading] = useState(false);
const navigate = useNavigate();

const handleInputChange = async(e)=>{
  //name : email value is like : abc
  const name = e.target.name;
  const value = e.target.value;
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
      const response = await signup(formData);  //in response we get status and data jwtToken ,name
      if (response.status === 201) {  //something was created that is 201  in backend
          const responseData = response.data;
          saveToken(responseData.jwtToken);
          navigate("/dashboard");
          enqueueSnackbar(`Welcome ${responseData.name}`, { variant: 'success', autoHideDuration: 5000 });
      }
  } catch (error) {
      if (error.response && error.response.status === 409) {
          enqueueSnackbar('User already exists!', { variant: 'error', autoHideDuration: 5000 });
      } else {
          enqueueSnackbar('Sign up failed!', { variant: 'error', autoHideDuration: 5000 });
      }
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
                  Sign Up
              </Typography>
                <Box component="form" onSubmit={handleSubmit}
                noValidate sx={{mt : 1}}>
                  <Grid container spacing={2}>
                      <Grid size={{xs : 12 , sm : 6}}>
                          <TextField
                          autoComplete="give-name"
                          name="firstName"
                          required
                          fullWidth
                          id="firstName"
                          label="First Name"
                          autoFocus
                          value={formData.firstName}
                          onChange={handleInputChange}
                        />
                      </Grid>
                      <Grid size={{xs : 12 , sm : 6}}>
                          <TextField
                          autoComplete="family-name"
                          name="lastName"
                          required
                          fullWidth
                          id="lastName"
                          label="Last Name"
                          autoFocus
                          value={formData.lastName}
                          onChange={handleInputChange}
                        />
                      </Grid>
                      <Grid size={{xs : 12}}>
                          <TextField
                          autoComplete="email"
                          name="email"
                          required
                          fullWidth
                          id="email"
                          label="Email Address"
                          autoFocus
                          value={formData.email}
                          onChange={handleInputChange}
                        />
                      </Grid>
                      <Grid size={{xs : 12}}>
                          <TextField
                          autoComplete="new-password"
                          name="password"
                          required
                          fullWidth
                          id="password"
                          label="Password"
                          autoFocus
                          value={formData.password}
                          onChange={handleInputChange}
                        />
                      </Grid>
                  </Grid>

                  <Button
                    type='submit'
                    fullWidth
                    variant='contained'
                    sx={{mt : 3 , mb : 2}}
                    disabled = {!formData.email || !formData.password ||
                      !formData.firstName ||
                      !formData.lastName
                      
                    }
                  >
                    {loading ? <CircularProgress color="success" size={24}/> : "Sign Up"}
                  </Button>
                  <Grid container>
                      <Grid>
                          <Link variant="body2" onClick={()=>navigate("/login")}>
                          {"Already have an account? Sign In"}
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

export default Signup
