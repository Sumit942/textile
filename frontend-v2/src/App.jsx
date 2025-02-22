import './assets/styles/main.css'
import { useEffect, useState } from 'react'
import { getNavigation } from './service/navigation'
import Navbar from './component/Navbar'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { Home } from './component/Home'
import { YarnForm } from './component/Yarn'
import { NoPage } from './component/NoPage'
import { Contact } from './component/Contact'
import { setupCSRF } from './service/api'
import { YarnList } from './component/YarnList'
import CompanyForm from './component/Company'
import OrderForm from './component/OrderForm'
import OrderFormVal from './component/OrderFormVal'
import OrderLists from './component/OrderLists'
// import Navbar from './component/NavTest'

function App() {
  const [navItems, setNavItems] = useState([])

  useEffect(() => {
    setupCSRF()
    loadNavigationPanel()
  }, [])
  
  const loadNavigationPanel = async () => {
    const navResponse = await getNavigation();
    console.log('navResponse: ' , navResponse)
    setNavItems(navResponse)
  }
  
  return (
    <div className='body'>
      {/* <NavTest items={navItems}/> */}
      <BrowserRouter >
        <Navbar items={navItems} />
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/contact' element={<Contact />} />
          <Route path='/material/yarn' element={<YarnForm />} />
          <Route path='/material/yarn/view' element={<YarnList />} />
          <Route path='/company/save' element={<CompanyForm />} />
          <Route path='/*' element={<NoPage />} />
          <Route path='/orders/save' element={<OrderFormVal />} />
          <Route path='/orders' element={<OrderLists />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
