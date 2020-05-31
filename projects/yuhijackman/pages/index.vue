<template>
        <div class="index">
            <TopPanel/>
            <div class="navigation-field">
                <div class="navigation__content navigation__content--left">
                    <nuxt-link to="#article" class="navigation__title">Story</nuxt-link>
                </div>
                <div class="navigation__content">
                    <nuxt-link to="#instruction" class="navigation__title">How it works</nuxt-link>
                </div>
                <div class="navigation__content">
                    <nuxt-link to="#news" class="navigation__title">News</nuxt-link>
                </div>
            </div>
            <article id="about">
                <TopExplanationPanel
                :color-name="'primary'"
                >
                    <template #title>
                        Story
                    </template>
                    <template #contents>
                        <ExplanationWithImage
                            :position="'second'"
                        >
                        <template #message>
                        <h2 class="message-title">
                            I'm not the only person who has started dozens of books and never finished them.
                        </h2>
                        <p class="message-content">
                            This is what I told myself when I saw tons of books on my bookshelf and noticed many of them aren't read through to the end.
                            And I wanted to create something to solve this problem. 
                        </p>
                        <p class="message-content">
                            Somehow I started to wonder how much money I spent on these unread books and calculated it.
                            The result put a guilt trip on me.
                            And I thought this sense of guilt may trigger off me picking up the unfinished books once again from my bookshelf.
                        </p>
                        <p class="message-content">
                            What we want is an app that automatically calculates it and helps me stop letting books pile up unread.
                            This is the original idea for Book-saver.
                        </p>
                        <p class="message-content">
                            Book-saver is a visual reminder of how much money you have spent on books and motivates you to read unfinished books.
                        </p>
                        </template>
                        <template #image>
                            <ImageWithColorfulBackground
                                :imageType="'pile'"
                                :color-name="'primary'"
                                :image-position="'right'"
                            />
                        </template>
                        </ExplanationWithImage>
                    </template>
                </TopExplanationPanel>
            </article>
            <article id="instruction">
                <TopExplanationPanel
                :color-name="'red'"
                >
                    <template #title>
                        How it works
                    </template>
                    <template #contents>
                        <ExplanationWithImage
                            :position="'first'"
                        >
                            <template #message>
                                <h2 class="message-title">
                                    Book-saver is very simple.
                                </h2>
                                <div class="message-content-wrapper">
                                    <h3>Step 1</h3>
                                    <p class="message-content">Add unread books on your bookshelf to the list.</p>
                                    <h3>Step 2</h3>
                                    <p class="message-content">Check how much money your unread books are worth.</p>
                                    <h3>Step 3</h3>
                                    <p class="message-content">Once you finished a book, update its status from "unread" to "read".</p>
                                </div>
                            </template>
                            <template #image>
                                <ImageWithColorfulBackground
                                    :imageType="'howtouse'"
                                    :color-name="'red'"
                                    :image-position="'left'"
                                />
                            </template>
                        </ExplanationWithImage>
                    </template>
                </TopExplanationPanel>
            </article>
            <article id="news">
                <TopExplanationPanel>
                    <template #title>
                        News
                    </template>
                    <template #contents>
                        <div class="nothing-to-display">
                        <p>Coming soon.</p>
                        </div>
                    </template>
                </TopExplanationPanel>
            </article>
        </div>
</template>

<script>
import TopExplanationPanel from '~/components/molecules/field/TopExplanationPanel'
import ExplanationWithImage from '~/components/molecules/field/ExplanationWithImage'
import ImageWithColorfulBackground from '~/components/atoms/image/ImageWithColorfulBackground'
import TopPanel from '~/components/organisms/panel/TopPanel'

export default {
  components: {
      TopPanel,
      TopExplanationPanel,
      ExplanationWithImage,
      ImageWithColorfulBackground
  }
}
</script>


<style lang="scss" scoped>
.index {
    overflow: hidden;
}
.navigation-field {
    display: flex;
    justify-content: center;
    border-bottom: 1px solid #cbd5e0;
}
.navigation {
    &__title {
        font-size: 1.5rem;
        color: rgb(36, 30, 18);
        position: relative;
        font-weight: 700;
        text-decoration: none;
        font-family: aktiv-grotesk, sans-serif;
    }
    &__content {
        text-align: center;
        padding: 1rem;
        width: 33%;
        flex: 1 0 0;
        border-right: 1px solid #cbd5e0;
        &--left {
            border-left: 1px solid #cbd5e0;
        }
    }
}

.nothing-to-display {
    display: flex;
    width: 100%;
    justify-content: center;
}

#news, #instruction, #about {
    font-size: 1.6rem;
    padding-top: 60px;
    padding-bottom: 60px;
}
#instruction {
  background-color: #f1f1f1;
  h1 {
    color:#FF5252;
  }
}

.message-title {
    font-size: 2rem;
    margin-bottom: 1.5rem;
    line-height: 1.3;
}

.message-content {
    font-size: 1.5rem;
}

.message-content-wrapper {
    h3 {
        color: #0097a7;
    }
    p {
        margin-bottom: 1.5rem;
    }
}
@media screen and (max-width: 600px) {
    .navigation-field {
        display: none;
    }
    .message-title {
        font-size: 1.5rem;
        margin-bottom: 1rem;
        line-height: 1.3;
    }

    .message-content {
        font-size: 1.3rem;
    }
}
</style>