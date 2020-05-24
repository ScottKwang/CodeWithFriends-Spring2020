# frozen_string_literal: true

class AvatarUploader < CarrierWave::Uploader::Base
  include CarrierWave::MiniMagick

  storage :fog


  # Create different versions of your uploaded files:
  version :thumb do
    process resize_to_fit: [100, 100]
  end

  version :medium do
    process resize_to_fill: [300, 300]
  end

  version :small do
    process resize_to_fill: [140, 140]
  end

  def extension_white_list
    %w(jpg jpeg gif png)
  end

end
