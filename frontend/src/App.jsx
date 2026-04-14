
import { Navigate, Route, Routes } from 'react-router-dom'
import Login from './pages/auth/login/Login'
import Signup from './pages/auth/signup/Signup'
import Header from './pages/header/Header'
import DashBoard from './pages/user/dashboard/DashBoard'
import CreatePoll from './pages/user/create-poll/CreatePoll'
import ViewMyPolls from './pages/user/view-my-polls/ViewMyPolls'
import ViewPollDetails from './pages/user/view-poll-details/ViewPollDetails'

function App() {

  return (
   <>
      <Header/>
      <Routes>
        <Route path = "/" element={<Navigate to="/login" replace />}></Route>
        <Route path = "/register" element={<Signup/>}></Route>
        <Route path = "/login" element={<Login/>}></Route>
        <Route path = "/dashboard" element={<DashBoard/>}></Route>
        <Route path = "/poll/create" element={<CreatePoll/>}></Route>
        <Route path = "/my-polls" element={<ViewMyPolls/>}></Route>
        <Route path = "/poll/:id/:view" element={<ViewPollDetails/>}></Route>
      </Routes>
   </>
  )
}

export default App
