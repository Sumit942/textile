import React, { useState } from "react";
import { addYarn } from "../service/yarn";

export const Yarn = () => {
  const [yarn, setYarn] = useState({
    id: null,
    type: "",
    companyName: "",
    description: "",
  });
  const [loading, setLoading] = useState(false)
  const [successMsg, setSuccessMsg] = useState(null);
  const [failureMsg, setFailureMsg] = useState(null);
  const [msgColor, setMsgColor] = useState('green')

  const handleSubmit = async (e) => {
    e.preventDefault();

    setLoading(true)
    const trimmedInput = {
        ...yarn,
        type: yarn.type.trim(),
        companyName: yarn.companyName.trim(),
        description: yarn.description.trim(),
    }

    const response = await addYarn(trimmedInput)
    if (response && response.status === 201) {
        setSuccessMsg(`Yarn Saved Successfully: ${response.data.id}-${response.data.type}`)
        setMsgColor('green')

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
        setFailureMsg(`System error in saving yarn: ${response.code}`)
        setMsgColor('red')

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
  };

  const handleClickResponse = (e) => {
    setSuccessMsg(null)
    setFailureMsg(null)
  }

  return (
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
            <div className='sm:col-span-2' >
            <div className={`grid p-4 justify-items-stretch h-auto bg-${msgColor}-200 h-16 rounded-md outline outline-1 -outline-offset-1 outline-${msgColor}-800`}>
              <button
                type="button"
                className={`justify-self-end rounded-md text-${msgColor}-800 hover:text-${msgColor}-600`}
                onClick={handleClickResponse}
              >
                <svg
                  className='h-6 w-6'
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth="1.5"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M6 18L18 6"
                  />
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M6 6l12 11"
                  />
                </svg>
              </button>
              <span className={`justify-self-center text-${msgColor}-800`}>{successMsg}{failureMsg}</span>
            </div>
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
                className="block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600"
                // required
                value={yarn.type}
                onChange={handleChange}
              />
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
                className="block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600"
                required
                value={yarn.companyName}
                onChange={handleChange}
              />
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
  );
};
