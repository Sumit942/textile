import './assets/styles/main.css'
import { useEffect, useState } from 'react'
import { getNavigation } from './service/navigation'
import Navbar from './component/Navbar'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { Home } from './component/Home'
import { Yarn } from './component/Yarn'
import { NoPage } from './component/NoPage'
import { Contact } from './component/Contact'
import { setupCSRF } from './service/api'
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
          <Route path='/material/yarn' element={<Yarn />} />
          <Route path='/*' element={<NoPage />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
