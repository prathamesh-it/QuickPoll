import { AppBar, Box, Button, IconButton, Toolbar, Typography } 
from '@mui/material'
import MenuIcon from '@mui/icons-material/Menu';
import React, { useEffect, useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { isTokenValid, removeToken } from '../../utility/Common';

const Header = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isUserLoggedIn , setIsUserLoggedIn] = useState(false);

  const handleSignOut = ()=>{
    navigate("/login");
    removeToken();
  }

  // Think of it like this:
  // "Hey React, every time URL changes
  //  please check if user is logged in
  //  and update the header accordingly"
  useEffect(()=>{
    const isLogedIn = isTokenValid();
    setIsUserLoggedIn(isLogedIn);
  },[location]);  //runs everytime location changes

  useEffect(()=>{
      const interval = setInterval(()=>{
        if(!isTokenValid()){
          setIsUserLoggedIn(false);
          handleSignOut();
        }
      },1800000);  //30 minutes = 1,800,000 milliseconds

      return ()=>clearInterval(interval);  //clean on unmount
  },[]);

  return (
   <>
      <Box sx = {{flexGrow : 1}}>
        <AppBar position='static'>
          <Toolbar>
            <IconButton
              size='large'
              edge='start'
              color='inherit'
              aria-label='menu'
              sx={{mr : 2}}
            >
              <MenuIcon/>

            </IconButton>
            {/* Used to display text with predefined styles */}
            <Typography
              variant='h6' component='div'
              sx={{flexGrow:1}}>
                QuickPoll
            </Typography>
            {isUserLoggedIn ? (
                <>
                    <Button component={Link} to="/dashboard" color="inherit">Dashboard</Button>
                    <Button component={Link} to="/poll/create" color="inherit">Post Poll</Button>
                    <Button component={Link} to="/my-polls" color="inherit">My Polls</Button>
                    <Button color="inherit" onClick={handleSignOut}>Logout</Button>
                </>
            ) : (
                <>
                    <Button component={Link} to="/login" color="inherit">Login</Button>
                    <Button component={Link} to="/register" color="inherit">Register</Button>
                </>
            )}
          </Toolbar>

        </AppBar>

      </Box>
   </>
  )
}

export default Header
