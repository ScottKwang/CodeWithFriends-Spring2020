import React from 'react'

const Pagination = ({ initialPage, handleNextPage, handlePrevPage }) => {
  return (

    <div className='container-fluid text-center'>
      <ul className='pagination justify-content-center'>
        <li className='page-item'><button className='page-link' disabled={initialPage === 1 ? 'disabled' : ''} onClick={handlePrevPage}>Previous</button></li>
        <li className='page-item'><button className='page-link' onClick={handleNextPage}>Next</button></li>
      </ul>
    </div>

  )
}

export default Pagination
