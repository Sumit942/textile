import { useEffect, useState } from 'react'
import { setupCSRF } from './service/api'
import { getStates } from './service/stateApi'
import Navbar from './service/navbar'

function App() {
  return (
    <>
      <Navbar />
    </>
  )
}

export default App
