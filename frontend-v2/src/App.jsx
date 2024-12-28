import './assets/styles/main.css'
import { useEffect, useState } from 'react'
import { getNavigation } from './service/navigation'
import NavTest from './component/NavTest'
import Navbar from './component/navbar'

function App() {
  const [navItems, setNavItems] = useState([])

  useEffect(() => {
    loadNavigationPanel()
  }, [])
  
  const loadNavigationPanel = async () => {
    const navResponse = await getNavigation();
    console.log('navResponse: ' , navResponse)
    setNavItems(navResponse)
  }
  
  return (
    <div className='body'>
      <Navbar items={navItems} />
      {/* <NavTest items={navItems}/> */}
    </div>
  )
}

export default App
