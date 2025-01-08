import React, { useEffect, useState } from "react";
import { addYarn, getYarn } from "../service/yarn";

export const Yarn = () => {
  const [yarn, setYarn] = useState({
    id: null,
    type: "",
    companyName: "",
    description: "",
  });
  const [yarnList, setYarnList] = useState([])
  const [loading, setLoading] = useState(false)
  const [successMsg, setSuccessMsg] = useState(null);
  const [failureMsg, setFailureMsg] = useState(null);
  const [errors, setErrors] = useState({})

  useEffect(() => {
    fetchYarn()
  }, [])
  
  const fetchYarn = async () => {
    const yarnListResponse = await getYarn();
    console.log('yarnListResponse: ', yarnListResponse.data)
    setYarnList(yarnListResponse.data)
  }

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (Object.keys(errors).length > 0) {
      setFailureMsg('Please check below errors')
      setTimeout(() =>{
        setFailureMsg(null)
      }, 5000)
      return;
    }

    setLoading(true)
    const trimmedInput = {
        ...yarn,
        type: yarn.type.trim(),
        companyName: yarn.companyName.trim(),
        description: yarn.description.trim(),
    }

    const response = await addYarn(trimmedInput)
    if (response && response.status === 201) {
        setSuccessMsg(`Yarn Saved Successfully: ${response.data.type} [${response.data.id}]`)

        setYarn({
            id: null,
            type: "",
            companyName: "",
            description: "",
        });

        setTimeout(() => {
            setSuccessMsg(null)
        }, 10000);
    } else {
        if (response && response.status === 400) {
          setFailureMsg('Please check below errors')
          setErrors(response.response.data.errorMessages)
          console.log('errors: ' , errors)
//           TODO: preSave key to be handled
        } else {
          setFailureMsg(`System error in saving yarn: ${response.code}`)
        }

        setTimeout(() => {
            setFailureMsg(null)
        }, 10000)
    }
    
    setLoading(false)
    

  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setYarn({
      ...yarn,
      [name]: value.trimStart(),
    });
    setErrors((previousErrors) => {
      const updatedErrors = {...previousErrors};
      delete updatedErrors[name];
      return updatedErrors;
    })
  };

  const handleClickResponse = (e) => {
    setSuccessMsg(null)
    setFailureMsg(null)
  }

  return (
    <div className="md:flex">
      <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="text-balance text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
            Yarn Quality
          </h2>
          <p className="mt-2 text-lg/8 text-gray-600">
            All yarn details should be added here
          </p>
        </div>
        <form onSubmit={handleSubmit} className="mx-auto mt-16 max-w-xl sm:mt-20">
          <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
            {successMsg || failureMsg ? (
              <div 
                className={`sm:col-span-2 top-full left-0 ${successMsg ? 'bg-green-100' : 'bg-red-100'} ${successMsg ? 'text-green-500' : 'text-red-500'} mt-1 rounded-md px-3 py-1`}
              >
                <span>{successMsg}{failureMsg}</span>
                <button
                  type="button"
                  className={`float-right ml-2 ${successMsg ? 'text-green-500' : 'text-red-500'} ${successMsg ? 'hover:text-green-700' : 'hover:text-red-700'}`}
                  onClick={handleClickResponse}
                >
                  <svg 
                    className="w-4 h-4" 
                    fill="none" 
                    stroke="currentColor" 
                    viewBox="0 0 24 24" 
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            ) : (
              ""
            )}
            <div className="sm:col-span-2">
              <label
                htmlFor="type"
                className="block text-sm/6 font-semibold text-gray-900"
              >
                Type / Quality
              </label>
              <div className="mt-2.5">
                <input
                  type="text"
                  name="type"
                  id="type"
                  autoComplete="yarnType"
                  className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.type ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                  required
                  value={yarn.type}
                  onChange={handleChange}
                />
                {errors.type ? <span className="text-red-500 text-sm mt-1 ml-1">{errors.type}</span> : ''}
              </div>
            </div>
            <div className="sm:col-span-2">
              <label
                htmlFor="companyName"
                className="block text-sm/6 font-semibold text-gray-900"
              >
                Company
              </label>
              <div className="mt-2.5">
                <input
                  type="text"
                  name="companyName"
                  id="companyName"
                  autoComplete="organization"
                  className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyName ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                  required
                  value={yarn.companyName}
                  onChange={handleChange}
                />
                {errors.companyName ? <span className="text-red-500 text-sm mt-1 ml-1">{errors.companyName}</span> : ''}
              </div>
            </div>
            <div className="sm:col-span-2">
              <label
                htmlFor="description"
                className="block text-sm/6 font-semibold text-gray-900"
              >
                Description
              </label>
              <div className="mt-2.5">
                <textarea
                  name="description"
                  id="description"
                  rows="2"
                  className="block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600"
                  value={yarn.description}
                  onChange={handleChange}
                ></textarea>
              </div>
            </div>
          </div>
          <div className="mt-10">
            <button
              type="submit"
              disabled={loading}
              className="block w-full rounded-md bg-indigo-600 px-3.5 py-2.5 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              {loading ? "Adding Yarn..." : "Add Yarn"}
            </button>
          </div>
        </form>
      </div>
      <div className="">
          <table className="min-w-full bg-white border border-gray-200">
            <thead className="bg-gray-200 text-gray-600 uppercase text-sm leading-normal">
              <tr>
                <th key='yarnSrNoTh' className="py-3 px-6 text-left">Sr.No</th>
                <th key='yarnTypeTh' className="py-3 px-6 text-left">Type</th>
                <th key='yarnCompanyTh' className="py-3 px-6 text-left">Company Name</th>
                <th key='yarnDescTh' className="py-3 px-6 text-left">Description</th>
              </tr>
            </thead>
            <tbody className="text-gray-600 text-sm font-light">
              {
                yarnList.map((yarnrow, index) => (
                  <tr key={index} className={`border-b border-gray-200 hover:bg-gray-100 ${index % 2 === 0 ? 'bg-gray-50' : ''}`}>
                      <td key={yarnrow.id} className="py-3 px-6">{index}</td>
                      <td key={yarnrow.type} className="py-3 px-6">{yarnrow.type}</td>
                      <td key={yarnrow.companyName} className="py-3 px-6">{yarnrow.companyName}</td>
                      <td key={yarnrow.description} className="py-3 px-6">{yarnrow.description}</td>
                  </tr>
                ))
              }
            </tbody>
          </table>
      </div>
    </div>
  );
};
