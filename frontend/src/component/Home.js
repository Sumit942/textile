import axios from 'axios'
import React, { useEffect, useState } from 'react'

function Home() {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchCompanyData = async () => {
      try {
        
        const response = await axios.get('company/searchByName/Shree');
        console.log('response', response)
      
      } catch (error) {
        console.error('Error fetching company data:', error)  
      }
    };

    fetchCompanyData()

  }, [])
  

  return (
    <div>Home sweet home</div>
  )
}

export default Home;
