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
import OrderFormVal from './component/OrderFormVal'
import OrderLists from './component/OrderLists'
import CompanyYarnOrder from './component/CompanyYarnOrder'
import FabricDesign from './component/FabricDesign'
import YarnFabricDesign from './component/YarnFabricDesign'
import OrderProductMapping from './component/OrderProductMapping'
import CompanyYarnOrderList from './component/CompanyYarnOrderList'
import YarnFabricDesignList from './component/YarnFabricDesignList'

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
          <Route path='/orders/view' element={<OrderLists />} />
          <Route path='/companyYarnOrder/save' element={<CompanyYarnOrder />} />
          <Route path="/companyYarnOrder/view" element={<CompanyYarnOrderList />} />
          <Route path='/fabricDesign' element={<FabricDesign />} />
          <Route path='/yarnFabricDesign/save' element={<YarnFabricDesign />} />
          <Route path='/yarnFabricDesign/view' element={<YarnFabricDesignList />} />
          <Route path='/orderProductMapping/save' element={<OrderProductMapping />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
