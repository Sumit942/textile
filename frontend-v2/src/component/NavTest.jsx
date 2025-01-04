import { useState } from "react";

function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const [activeSubmenu, setActiveSubmenu] = useState(null);

  const handleSubMenuToggle = (id) => {
    setActiveSubmenu(id === activeSubmenu ? null : id); 
  };
  
  return (
    <>
      <nav className="bg-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <img className="h-8 w-8" src="vite.svg" alt="Logo" />
            </div>
          </div>
          <div className="-mr-2 flex md:hidden">
            <button 
              type="button" 
              className="bg-gray-800 inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
              aria-controls="mobile-menu"
              aria-expanded="false"
              onClick={() => setIsOpen(!isOpen)}
            >
              <span className="sr-only">Open main menu</span>
              {/* Menu icon */}
            </button>
          </div>
        </div>
      </div>

      <div className={`md:hidden ${isOpen ? 'block' : 'hide'}`}>
        <div className="px-2 pt-2 pb-3 space-y-1">
          <ul className="space-y-1">
            <li>
              <a href="#" className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Home</a>
            </li>
            <li>
              <a href="#" className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">About</a>
            </li>
            <li>
              <button 
                onClick={() => handleSubMenuToggle('services')} 
                className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
              >
                Services 
                {activeSubmenu === 'services' && (
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-4 h-4 ml-1">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5" />
                  </svg>
                )}
              </button>
              {activeSubmenu === 'services' && (
                <ul className="ml-4 mt-2">
                  <li className="p-2">Sub Service 1</li>
                  <li className="p-2">Sub Service 2</li>
                </ul>
              )}
            </li>
            <li>
              <a href="#" className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Contact</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    </>
  );
}
export default Navbar;
