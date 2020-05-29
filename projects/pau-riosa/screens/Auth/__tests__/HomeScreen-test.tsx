/**
 * @format
 */

import 'react-native';
import { shallow } from 'enzyme';
import React from 'react';
import HomeScreen from '../HomeScreen';

// Note: test renderer must be required after react-native.
import renderer from 'react-test-renderer';

const createTestProps = (props: object) => ({
  navigation: {
    navigate: jest.fn() 
  },
  ...props,
});

describe('HomeScreen', () => {
  const props = createTestProps({});
  const wrapper = shallow<HomeScreen>(<HomeScreen {...props} />);

  describe('rendering correctly', () => {
    it('should render a <View />', () => {
      expect(wrapper.find('View')).toHaveLength(3);
    });
  });
});
