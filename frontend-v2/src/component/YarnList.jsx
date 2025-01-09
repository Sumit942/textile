import { useEffect, useState } from "react";
import { getYarn } from "../service/yarn";
import { Link } from "react-router-dom";

export const YarnList = () => {
    const [rowData, setRowData] = useState([]);
  
    useEffect(() => {
      fetchYarn()
    }, [])
    
    const fetchYarn = async () => {
      const yarnListResponse = await getYarn();
      console.log('yarnListResponse: ', yarnListResponse.data)
      setRowData(yarnListResponse.data)
    }
  
  
    return (
      <div className="w-full bg-gray-100 px-3 py-5 sm:py-10 lg:px-8">
          <h1 className="text-xl mb-2">Yarn List</h1>
          <div className="overflow-auto rounded-lg shadow">  
          <table className="w-full">
            <thead className="bg-gray-50  border-b-2 border-gray-200">
              <tr>
                <th className="p-3 text-sm font-semibold tracking-wide text-left" key='id'>S.No</th>
                <th className="w-40 sm:w-auto p-3 text-sm font-semibold tracking-wide text-left" key='type'>Type</th>
                <th className="p-3 text-sm font-semibold tracking-wide text-left" key='companyName'>Company Name</th>
                <th className="p-3 text-sm font-semibold tracking-wide text-left" key='rate'>Rate</th>
                <th className="p-3 text-sm font-semibold tracking-wide text-left" key='description'>Description</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {
                rowData.map((row, index) => (
                  <tr key={index} className={index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}>
                    <td key='id' className="p-3 text-sm text-gray-700 h-10">
                      {/* <a href="#" className="font-bold text-blue-500 hover:underline">{index + 1}</a> */}
                      <Link to={'/material/yarn'} state={{yarnId : row.id}} key={index} className="font-bold text-blue-500 hover:underline" >{index + 1}</Link>
                    </td>
                    <td key='type' className="p-2 text-sm text-gray-700">{row.type}</td>
                    <td key='companyName' className="p-2 text-sm text-gray-700">{row.companyName}</td>
                    <td key='rate' className="p-2 text-sm text-gray-700">{row.rate}</td>
                    <td key='description' className="p-2 text-sm text-gray-700">{row.description}</td>
                  </tr>
                ))
              }
            </tbody>
          </table>
          </div>
      </div>
    )
  };