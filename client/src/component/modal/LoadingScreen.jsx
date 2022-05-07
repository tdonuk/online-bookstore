import React from 'react';
import { FaRegHourglass } from 'react-icons/fa';

class LoadingScreen extends React.Component {
  constructor() {
    super();
    this.state = {
      someKey: 'someValue'
    };
  }

  render() {
    return(
        <div className='modal-background'>
            <div className="flex flex-direction-column loading-container">
                <FaRegHourglass className='loading-spin' style={{color: 'antiquewhite', fontSize: '70px', margin: '10px auto'}}/>
                <h2 className='flex-center-align marign-auto secondary loading-scale'>Yükleniyor..</h2>
            </div>
        </div>
    );
  }

  componentDidMount() {
    this.setState({
      someKey: 'otherValue'
    });
  }
}

export default LoadingScreen;
